package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.FateAction;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.PoisonCoatedPower;

public class Hivemind
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Hivemind";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LizardSkl.png";
    private static final int COST = 1;
//    private static final int ENVENOM_AMT = 2;
//    private static final int UPGRADE_ENVENOM_AMT = 1;

    public Hivemind() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
//        this.magicNumber = this.baseMagicNumber = ENVENOM_AMT;
        this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        formEffect(InsectGene.ID, () -> addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p))));
        if (this.upgraded) {
            formEffect(LymeanGene.ID, () -> addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p))));
        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int count = (int) p.hand.group.stream().filter(card -> Drone.ID.equals(card.cardID)).count();
                addToTop(new FateAction(count));
                this.isDone = true;
            }
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new Hivemind();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeMagicNumber(UPGRADE_ENVENOM_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if ((!upgraded && isPlayerInThisForm(InsectGene.ID)) ||
                (upgraded && isPlayerInTheseForms(InsectGene.ID, LymeanGene.ID))) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}