package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.orbs.SuccubusGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.LustPower;

public class Charm
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Charm";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SuccubusSkl.png";
    private static final int COST = 1;
    private static final int REDUCTION_AMT = 2;
    private static final int UPGRADE_REDUCTION_AMT = 2;
    private static final int FORM_VULNERABLE = 2;
    private static final int BLOCK_AMT = 1;

    public Charm() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.COMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = REDUCTION_AMT;
        this.block = this.baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = this.magicNumber;
        addToBot(new GainBlockAction(p, this.block));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new LustPower(m, amount), amount));
        formEffect(SuccubusGene2.ID, () -> addToBot(new ApplyPowerAction(m, p, new VulnerablePower(
                m, FORM_VULNERABLE, false))));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Charm();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_REDUCTION_AMT);
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            this.initializeDescription();
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