package evolutionmod.cards;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.SpotWeaknessAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.LustPower;

import java.util.List;

public class Pheromones2
        extends BaseEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:Pheromones";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Pheromones.png";
    private static final int COST = 0;
    private static final int FORM_DEBUFF_AMT = 2;
    private static final int UPGRADE_DEBUFF_AMT = 1;

    public Pheromones2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.magicNumber = this.baseMagicNumber = FORM_DEBUFF_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.getMonsters().monsters.stream()
                .filter(mo -> !mo.isDeadOrEscaped())
                .filter(mo -> mo.getIntentBaseDmg() >= 0)
                .forEach(mo -> {
                    addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false)));
                    addToBot(new ApplyPowerAction(mo, p, new LustPower(mo, magicNumber)));
                });

        this.exhaust = !formEffect(PlantGene.ID);
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                exhaust = true;
                this.isDone = true;
            }
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new Pheromones2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DEBUFF_AMT);
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            this.initializeDescription();
        }
    }

    @Override
    public int getNumberOfGlows() {
        return 2;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return true;
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        switch (glowIndex) {
            case 0:
                return isPlayerInThisForm(PlantGene.ID) ? PlantGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
            case 1:
                return isPlayerInThisForm(SuccubusGene.ID, PlantGene.ID) ? SuccubusGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
            default:
                return BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}