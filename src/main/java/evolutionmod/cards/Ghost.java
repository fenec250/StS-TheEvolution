package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import evolutionmod.orbs.ShadowGene2;
import evolutionmod.patches.EvolutionEnum;

public class Ghost
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Ghost";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ShadowSkl.png";
    private static final int COST = 2;
    private static final int GENES_AMT = 1;
    private static final int UPGRADE_GENES_AMT = 1;

    public Ghost() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = GENES_AMT;
        this.exhaust = true;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isPlayerInThisForm(ShadowGene2.ID)) {
            addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1)));
        }
        formEffect(ShadowGene2.ID);
        for (int i = 0; i < this.magicNumber; ++i)
            addToBot(new ShadowGene2().getChannelAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new Ghost();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_GENES_AMT);
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(ShadowGene2.ID)) {
            this.glowColor = ShadowGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void initializeDescription() {
        this.rawDescription = this.magicNumber == 1 ?  DESCRIPTION
                : EXTENDED_DESCRIPTION[0] + this.magicNumber + EXTENDED_DESCRIPTION[1];
        super.initializeDescription();
    }
}