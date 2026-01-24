package evolutionmod.cardsV1;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.GlowingCard;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.orbsV1.*;

public class Battleborn
        extends BaseEvoCard implements GlowingCard {
    public static final String cardID = "Battleborn";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CentaurPower.png";
    private static final int COST = 1;
    private static final int STRENGTH_AMT = 1;
    private static final int UPGRADE_STRENGTH_AMT = 1;
    private static final int FORMS_STRENGTH_AMT = 1;

    public Battleborn() {
        super(ID, NAME, new RegionName("red/power/inflame"), COST, DESCRIPTION,
                CardType.POWER, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = STRENGTH_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new StrengthPower(p, this.magicNumber)));
        formEffect(CentaurGene.ID, () -> {
            if (!this.upgraded) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                        new StrengthPower(p, FORMS_STRENGTH_AMT)));
            } else {
                formEffect(BeastGene.ID, () ->
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                                new StrengthPower(p, FORMS_STRENGTH_AMT))));
            }
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new Battleborn();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_STRENGTH_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            if (customTooltips == null) {
                customTooltips = this.getCustomTooltips();
            }
            this.customTooltips.add(new TooltipInfo("Form -> Form", "Channel the second Gene only if the first is present. NL Apply the effect only if both Genes are present."));
            this.initializeDescription();
        }
    }

    @Override
    public int getNumberOfGlows() {
        return upgraded ? 2 : 1;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return true;
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        switch (glowIndex) {
            case 0:
                return isPlayerInThisForm(CentaurGene.ID) ? CentaurGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
            case 1:
            return isPlayerInThisForm(BeastGene.ID, CentaurGene.ID) ? BeastGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
        }
        return BLUE_BORDER_GLOW_COLOR.cpy();
    }
}