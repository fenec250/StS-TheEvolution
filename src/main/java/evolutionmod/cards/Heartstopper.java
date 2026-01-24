package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import evolutionmod.orbs.SuccubusGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.LustPower;

public class Heartstopper
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Heartstopper";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Aphrodisiac.png";
    private static final int COST = 1;
    private static final int VULNERABLE_AMT = 2;
    private static final int UPGRADE_VULNERABLE_AMT = 1;

    public Heartstopper() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = VULNERABLE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new LustPower(m, this.magicNumber)));
        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber)));
//        BaseEvoCard.formEffect(SuccubusGene.ID,
//                () -> addToBot(new ApplyPowerAction(m, p, new LustPower(m, this.magicNumber))));
        formEffect(SuccubusGene2.ID, () ->
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        m.powers.stream()
                                .filter(pow -> pow.ID.equals(PoisonPower.POWER_ID))
                                .findAny()
                                .ifPresent(AbstractPower::atStartOfTurn);
                        this.isDone = true;
                    }
                }));
//        AbstractCard status = p.hand.getRandomCard(CardType.STATUS, true);
//        if (status != null) {
//            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(status, p.hand));
//        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Heartstopper();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeBlock(UPGRADE_BLOCK_AMT);
            this.upgradeMagicNumber(UPGRADE_VULNERABLE_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(SuccubusGene2.ID)) {
            this.glowColor = SuccubusGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}